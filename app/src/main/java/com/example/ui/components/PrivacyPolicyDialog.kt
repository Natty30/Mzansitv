package com.example.ui.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ads.AdMobManager
import com.example.ui.theme.BackgroundObsidian
import com.example.ui.theme.MzansiGold
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

const val PRIVACY_POLICY_URL = "https://github.com/Natty30/Mzansitv/blob/main/PRIVACY_POLICY.md"

@Composable
fun PrivacyPolicyDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f)
                .testTag("privacy_policy_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = BackgroundObsidian,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardHighlight)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(MzansiGold.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PrivacyTip,
                                contentDescription = "Privacy Policy Icon",
                                tint = MzansiGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Privacy Policy",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Mzansi TV • Updated September 2026",
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog",
                            tint = TextSecondary
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = SurfaceCardHighlight
                )

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(end = 4.dp)
                ) {
                    PolicySection(
                        title = "1. Introduction & Overview",
                        body = "Mzansi TV provides live public broadcast television streaming for South Africa. We respect your privacy and are committed to protecting your personal data in compliance with the South African Protection of Personal Information Act (POPIA), the EU General Data Protection Regulation (GDPR), and Google Play Developer Program policies."
                    )

                    PolicySection(
                        title = "2. Information We Collect",
                        body = "• Direct Personal Information: Mzansi TV does NOT require user registration, logins, passwords, names, or email addresses to watch channels.\n" +
                                "• Local Preferences: Favorited channels are stored strictly locally on your device using a local Room SQLite database and are never uploaded to any remote server.\n" +
                                "• Network Information: IP address and standard connection metadata are transmitted automatically to network providers to deliver HLS video streams."
                    )

                    PolicySection(
                        title = "3. Advertising & Google AdMob",
                        body = "Mzansi TV uses Google AdMob to display banner and interstitial advertisements.\n" +
                                "Google AdMob may collect and process:\n" +
                                "• Google Advertising ID (GAID/AAID)\n" +
                                "• Approximate coarse location based on IP address\n" +
                                "• Device hardware characteristics and operating system version\n" +
                                "• Ad interaction and impression diagnostic telemetry\n" +
                                "This information is used to serve personalized or non-personalized ads, prevent fraudulent traffic, and monitor ad performance according to Google's Privacy Policy (https://policies.google.com/privacy)."
                    )

                    PolicySection(
                        title = "4. Consent Management (Google UMP)",
                        body = "In jurisdictions requiring consent (such as the EEA, UK, and South Africa), we implement Google's User Messaging Platform (UMP) SDK. Users can grant, deny, or customize consent for ad personalization and measurement at any time via the 'Manage Privacy Choices' button below."
                    )

                    PolicySection(
                        title = "5. Permissions Used",
                        body = "• INTERNET: Essential to stream live television channels and fetch advertisement data.\n" +
                                "• ACCESS_NETWORK_STATE: Used to detect Wi-Fi vs mobile cellular connectivity to manage buffer health.\n" +
                                "No camera, microphone, contact, location, or broad storage permissions are requested or used."
                    )

                    PolicySection(
                        title = "6. Data Retention & Security",
                        body = "All video streaming data in transit is encrypted using standard HTTPS / TLS encryption. No user accounts or personal profiles are created, stored, or retained by Mzansi TV."
                    )

                    PolicySection(
                        title = "7. Children's Privacy",
                        body = "Mzansi TV is a general-audience television streaming app and does not knowingly collect personal information from children under the age of 13. SABC Education programming is offered for public educational viewing."
                    )

                    PolicySection(
                        title = "8. Contact Us",
                        body = "If you have questions regarding this Privacy Policy or your data privacy rights, please contact the developer:\n" +
                                "Developer: Natty30\n" +
                                "Email: zingelwayocivilandplant@gmail.com"
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = SurfaceCardHighlight
                )

                // Actions: Manage Consent + Open Web Link + Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (activity != null) {
                        OutlinedButton(
                            onClick = {
                                AdMobManager.showPrivacyOptionsForm(activity)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Consent Settings",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MzansiGold),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open Web URL",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Web Policy",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(
            text = title,
            color = MzansiGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = body,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}
