package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.app.PendingIntent.getActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //displayQuantity(quantity);
        // displayOrderSummary(NumberFormat.getCurrencyInstance().format(calculatePrice(false, false)));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream_cb);
        boolean hasWhippedCream = whippedCream.isChecked();

        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_cb);
        boolean hasChocolate = chocolate.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        EditText customerName = (EditText) findViewById(R.id.name_edittext);
        String name = customerName.getText().toString();
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);
        String subject = String.format(getString(R.string.order_summary_email_subject), name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        //displayOrderSummary(priceMessage);
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream true if order has whipped cream, false otherwise
     * @param hasChocolate true if order has chocolate, false otherwise
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        final int UNIT_COST = 5;

        // Price of 1 cup of coffee
        int basePrice = UNIT_COST;

        // Add $1 if the user wants whipped cream
        if (hasWhippedCream) {
            basePrice += 1;
        }

        // Add $2 if the user wants chocolate
        if (hasChocolate) {
            basePrice += 2;
        }

        // Calculate the total order price by multiplying by the number of cups of coffee
        return quantity * basePrice;
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.error_morethan100cups),
                    Toast.LENGTH_SHORT).show();

            // Exit this method early because there is nothing left to do
            return;
        }

        displayQuantity(++quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.error_lessthan1cup),
                    Toast.LENGTH_SHORT).show();

            // Exit this method early because there is nothing left to do
            return;
        }

        displayQuantity(--quantity);
    }

    /**
     * Create summary of the order.
     *
     * @param price of the order
     * @param hasWhippedCream true if order has whipped cream, false otherwise
     * @param hasChocolate true if order has chocolate, false otherwise
     * @return the price message
     */
    private String createOrderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolate) {

        String summary = String.format(getString(R.string.order_summary_name) +
                                        getString(R.string.order_summary_whipped_cream) +
                                        getString(R.string.order_summary_chocolate) +
                                        getString(R.string.order_summary_quantity) +
                                        getString(R.string.order_summary_price) +
                                        getString(R.string.thank_you),
                                        name,
                                        hasWhippedCream ? "true" : "false",
                                        hasChocolate ? "true" : "false",
                                        quantity,
                                        NumberFormat.getCurrencyInstance().format(price));

        return summary;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText(Integer.toString(number));
    }

    /**
     * This method displays the given text on the screen.
     */
//    private void displayOrderSummary(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//    }
}
