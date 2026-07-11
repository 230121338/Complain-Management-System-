package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import tut.ac.za.complaint.dao.ComplaintDAO;
import tut.ac.za.complaint.model.Complaint;
import tut.ac.za.complaint.model.ImageData;
import tut.ac.za.complaint.model.User;

/**
 * Lets the receptionist download all complaints as a PDF document, with each
 * complaint's picture embedded in the row.
 */
public class DownloadComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"Receptionist".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String query = request.getParameter("q");
        List<Complaint> complaints;
        try {
            complaints = (query == null || query.trim().isEmpty())
                    ? complaintDAO.findAll()
                    : complaintDAO.searchByStudentName(query.trim());
        } catch (SQLException e) {
            throw new ServletException("Unable to load complaints", e);
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"complaints.pdf\"");

        Document document = new Document(PageSize.A4.rotate(), 24, 24, 30, 30);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Complaint Management System \u2014 Complaints Report", titleFont);
            title.setSpacingAfter(12f);
            document.add(title);

            PdfPTable table = new PdfPTable(new float[]{2f, 3.5f, 2f, 1.2f, 1.2f, 1.5f, 1.5f, 1.5f});
            table.setWidthPercentage(100);

            for (String header : new String[]{"Student", "Complaint", "Picture", "Block", "Room",
                    "Status", "Viewed Date", "Fixed Date"}) {
                table.addCell(headerCell(header));
            }

            for (Complaint c : complaints) {
                table.addCell(text(c.getStudentName()));
                table.addCell(text(c.getComplaint()));
                table.addCell(pictureCell(c));
                table.addCell(text(c.getBlock()));
                table.addCell(text(c.getRoom()));
                table.addCell(text(c.getStatus()));
                table.addCell(text(c.getViewedDate() == null ? "-" : c.getViewedDate().toString()));
                table.addCell(text(c.getFixedDate() == null ? "-" : c.getFixedDate().toString()));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new ServletException("Unable to build complaints PDF", e);
        }
    }

    private PdfPCell pictureCell(Complaint c) throws ServletException {
        if (c.isHasImage()) {
            try {
                ImageData data = complaintDAO.findImage(c.getComplaintId());
                if (data != null) {
                    Image image = Image.getInstance(data.getData());
                    image.scaleToFit(70f, 55f);
                    PdfPCell cell = new PdfPCell(image, false);
                    cell.setPadding(4f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    return cell;
                }
            } catch (SQLException | IOException | RuntimeException e) {
                // fall through to the placeholder text below
            }
        }
        return text("No picture");
    }

    private static PdfPCell headerCell(String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cell.setPadding(5f);
        return cell;
    }

    private static PdfPCell text(String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value == null ? "" : value,
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setPadding(5f);
        return cell;
    }
}
