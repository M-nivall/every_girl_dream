package com.example.Varsani.Suppliers;

import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Varsani.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SupplyReceipt extends AppCompatActivity {

    private WebView webViewReceipt;
    private Button btnPrint;

    private String requestID, urgency, status, unitPrice, totalPrice,
            supplierName, supplierPhone, quantityNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Receipt");

        webViewReceipt = findViewById(R.id.webViewReceipt);
        btnPrint = findViewById(R.id.btnPrint);

        // Get data from Intent
        requestID      = getIntent().getStringExtra("requestID");
        urgency        = getIntent().getStringExtra("urgency");
        status         = getIntent().getStringExtra("status");
        unitPrice      = getIntent().getStringExtra("unitPrice");
        totalPrice     = getIntent().getStringExtra("totalPrice");
        quantityNeeded = getIntent().getStringExtra("quantityNeeded");
        supplierName   = getIntent().getStringExtra("supplierName");
        supplierPhone  = getIntent().getStringExtra("supplierPhone");

        String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

        String html = buildReceiptHtml(date);

        webViewReceipt.getSettings().setJavaScriptEnabled(false);
        webViewReceipt.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);

        btnPrint.setOnClickListener(v -> printReceipt());
    }

    private void printReceipt() {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webViewReceipt.createPrintDocumentAdapter("Supply_Receipt_" + requestID);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        printManager.print("Supply Receipt", printAdapter, builder.build());
    }

    private String buildReceiptHtml(String date) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'/>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<style>" +
                "  @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600;700&family=Lato:wght@300;400;700&display=swap');" +
                "  * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "  body { font-family: 'Lato', sans-serif; background: #f5f0eb; padding: 20px; }" +
                "  .receipt { max-width: 680px; margin: 0 auto; background: #fff; border-radius: 4px; overflow: hidden; box-shadow: 0 4px 24px rgba(0,0,0,0.10); }" +
                "  .header { background: #1a1a2e; color: #fff; padding: 36px 40px 28px; text-align: center; position: relative; }" +
                "  .header::after { content: ''; display: block; height: 6px; background: linear-gradient(90deg, #c9a84c, #f0d080, #c9a84c); margin-top: 20px; margin-left: -40px; margin-right: -40px; }" +
                "  .company-name { font-family: 'Playfair Display', serif; font-size: 32px; letter-spacing: 2px; color: #f0d080; margin-bottom: 6px; }" +
                "  .company-tagline { font-size: 12px; letter-spacing: 3px; text-transform: uppercase; color: #aaa; margin-bottom: 14px; }" +
                "  .company-info { font-size: 13px; color: #ccc; line-height: 1.8; }" +
                "  .receipt-title-bar { background: #c9a84c; text-align: center; padding: 10px; }" +
                "  .receipt-title-bar span { font-family: 'Playfair Display', serif; font-size: 15px; letter-spacing: 4px; text-transform: uppercase; color: #1a1a2e; font-weight: 700; }" +
                "  .body { padding: 32px 40px; }" +
                "  .meta-row { display: flex; justify-content: space-between; margin-bottom: 24px; }" +
                "  .meta-block { font-size: 13px; color: #555; line-height: 1.7; }" +
                "  .meta-block strong { display: block; font-size: 11px; letter-spacing: 1.5px; text-transform: uppercase; color: #c9a84c; margin-bottom: 2px; }" +
                "  .divider { border: none; border-top: 1px solid #e8e0d4; margin: 20px 0; }" +
                "  .section-label { font-size: 11px; letter-spacing: 2px; text-transform: uppercase; color: #c9a84c; margin-bottom: 14px; font-weight: 700; }" +
                "  table { width: 100%; border-collapse: collapse; margin-bottom: 8px; }" +
                "  thead th { font-size: 11px; letter-spacing: 1.5px; text-transform: uppercase; color: #888; padding: 8px 0; border-bottom: 2px solid #e8e0d4; text-align: left; }" +
                "  thead th:last-child { text-align: right; }" +
                "  tbody td { padding: 12px 0; font-size: 14px; color: #333; border-bottom: 1px solid #f0ece5; vertical-align: middle; }" +
                "  tbody td:last-child { text-align: right; font-weight: 700; color: #1a1a2e; }" +
                "  .totals { background: #f9f6f1; border-radius: 4px; padding: 18px 20px; margin-top: 20px; }" +
                "  .total-row { display: flex; justify-content: space-between; font-size: 13px; color: #555; margin-bottom: 8px; }" +
                "  .total-row.grand { font-size: 18px; font-weight: 700; color: #1a1a2e; border-top: 2px solid #c9a84c; padding-top: 12px; margin-top: 8px; }" +
                "  .badge { display: inline-block; padding: 4px 14px; border-radius: 20px; font-size: 11px; font-weight: 700; letter-spacing: 1px; text-transform: uppercase; }" +
                "  .badge-paid { background: #e8f5e9; color: #2e7d32; }" +
                "  .badge-other { background: #fff3e0; color: #e65100; }" +
                "  .supplier-box { background: #1a1a2e; border-radius: 4px; padding: 18px 22px; margin-top: 24px; }" +
                "  .supplier-box .section-label { color: #c9a84c; }" +
                "  .supplier-row { display: flex; justify-content: space-between; font-size: 14px; color: #eee; line-height: 1.9; }" +
                "  .supplier-row span:first-child { color: #aaa; font-size: 12px; }" +
                "  .footer { text-align: center; padding: 24px 40px 32px; }" +
                "  .footer p { font-size: 12px; color: #aaa; line-height: 1.7; }" +
                "  .footer .thank-you { font-family: 'Playfair Display', serif; font-size: 18px; color: #c9a84c; margin-bottom: 8px; }" +
                "  .watermark { font-size: 10px; color: #ccc; letter-spacing: 2px; text-transform: uppercase; margin-top: 16px; }" +
                "  @media print { body { background: #fff; padding: 0; } .receipt { box-shadow: none; } }" +
                "</style></head><body>" +
                "<div class='receipt'>" +

                // Header
                "  <div class='header'>" +
                "    <div class='company-name'>Girls Dream</div>" +
                "    <div class='company-tagline'>Girl Child &bull; NO to FGM</div>" +
                "    <div class='company-info'>" +
                "      Tel: 0709090976 &nbsp;&bull;&nbsp; Email: info@girlsdream.co.ke<br/>" +
                "      P.O. Box 00100, Isiolo, Kenya &nbsp;&bull;&nbsp; CODE: WRT5GFHTF" +
                "    </div>" +
                "  </div>" +

                "  <div class='receipt-title-bar'><span>Supply Receipt</span></div>" +

                "  <div class='body'>" +

                // Meta row
                "    <div class='meta-row'>" +
                "      <div class='meta-block'><strong>Receipt No.</strong>#REC-" + requestID + "</div>" +
                "      <div class='meta-block' style='text-align:right'><strong>Date</strong>" + date + "</div>" +
                "    </div>" +
                "    <div class='meta-row'>" +
                "      <div class='meta-block'><strong>Request ID</strong>" + requestID + "</div>" +
                "      <div class='meta-block' style='text-align:right'><strong>Urgency</strong>" + urgency + "</div>" +
                "    </div>" +

                "    <hr class='divider'/>" +

                // Items table
                "    <div class='section-label'>Order Details</div>" +
                "    <table>" +
                "      <thead><tr><th>Description</th><th>Qty</th><th>Unit Price</th><th>Amount</th></tr></thead>" +
                "      <tbody>" +
                "        <tr>" +
                "          <td>Sanitary Towels</td>" +
                "          <td>" + quantityNeeded + " Units</td>" +
                "          <td>Ksh " + unitPrice + "</td>" +
                "          <td>Ksh " + totalPrice + "</td>" +
                "        </tr>" +
                "      </tbody>" +
                "    </table>" +

                // Totals
                "    <div class='totals'>" +
                "      <div class='total-row'><span>Subtotal</span><span>Ksh " + totalPrice + "</span></div>" +
                "      <div class='total-row'><span>Tax (0%)</span><span>Ksh 0.00</span></div>" +
                "      <div class='total-row grand'><span>Total Paid</span><span>Ksh " + totalPrice + "</span></div>" +
                "    </div>" +

                // Payment status
                "    <div style='margin-top:16px;'>" +
                "      <span class='badge " + (status.equals("Paid") ? "badge-paid" : "badge-other") + "'>" + status + "</span>" +
                "    </div>" +

                // Supplier info
                "    <div class='supplier-box'>" +
                "      <div class='section-label'>Supplier Information</div>" +
                "      <div class='supplier-row'><span>Supplier Name</span><span>" + supplierName + "</span></div>" +
                "      <div class='supplier-row'><span>Phone</span><span>" + supplierPhone + "</span></div>" +
                "    </div>" +

                "  </div>" +

                // Footer
                "  <div class='footer'>" +
                "    <div class='thank-you'>Thank You!</div>" +
                "    <p>This is an official supply receipt from Girls Dream.<br/>For any queries, contact us at 0709090976 or info@girlsdream.co.ke</p>" +
                "    <div class='watermark'>Girls Dream &mdash; Official Document</div>" +
                "  </div>" +

                "</div></body></html>";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}