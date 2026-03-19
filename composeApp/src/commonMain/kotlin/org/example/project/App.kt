package org.example.project

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import tugas4_pam.composeapp.generated.resources.Res
import tugas4_pam.composeapp.generated.resources.profile_pict

private val PrimaryBlue   = Color(0xFF1A73E8)
private val AccentCyan    = Color(0xFF00BCD4)
private val SurfaceLight  = Color(0xFFF8FAFF)
private val CardWhite     = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF1C1C2E)
private val TextSecondary = Color(0xFF6B7280)
private val GreenOnline   = Color(0xFF22C55E)
private val GradientStart = Color(0xFF1A73E8)
private val GradientEnd   = Color(0xFF6C63FF)

private val DarkBackground = Color(0xFF0F1117)
private val DarkSurface    = Color(0xFF1A1D26)
private val DarkSurfaceVar = Color(0xFF252836)
private val DarkOnSurface  = Color(0xFFE8EAF0)
private val DarkOnSurfVar  = Color(0xFF9EA3B0)
private val DarkPrimary    = Color(0xFF7AB3F5)
private val DarkOutline    = Color(0xFF2E3347)

data class UserProfile(
    val name        : String = "Muhammad Fadhilah Akbar",
    val title       : String = "Mobile Developer · ITERA '23",
    val status      : String = "Aktif Belajar",
    val bio         : String = "Mahasiswa Teknik Informatika ITERA angkatan 2023. " +
            "Fokus pada Mobile Development dan UI/UX Design. " +
            "Passionate dalam membangun aplikasi multiplatform dengan Kotlin.",
    val email       : String = "muhammad.123140003@student.itera.ac.id",
    val phone       : String = "+62 853 4258 6196",
    val location    : String = "Lampung, Indonesia",
    val institution : String = "Institut Teknologi Sumatera",
    val angkatan    : String = "2023",
    val prodi       : String = "IF",
    val projects    : String = "3+"
)

data class ProfileUiState(
    val profile      : UserProfile = UserProfile(),
    val isEditing    : Boolean     = false,
    // Temporary edit fields — belum disimpan ke profile
    val editName     : String      = "",
    val editTitle    : String      = "",
    val editBio      : String      = "",
    val editEmail    : String      = "",
    val editPhone    : String      = "",
    val editLocation : String      = "",
    val isDarkMode   : Boolean     = false,
    val errorMessage : String?     = null,
    val saveSuccess  : Boolean     = false,
    val showContact  : Boolean     = false
)

class ProfileViewModel {
    var uiState by mutableStateOf(ProfileUiState())
        private set

    fun toggleContact() {
        uiState = uiState.copy(showContact = !uiState.showContact)
    }

    fun startEditing() {
        val p = uiState.profile
        uiState = uiState.copy(
            isEditing    = true,
            editName     = p.name,
            editTitle    = p.title,
            editBio      = p.bio,
            editEmail    = p.email,
            editPhone    = p.phone,
            editLocation = p.location,
            saveSuccess  = false,
            errorMessage = null
        )
    }

    fun cancelEditing() {
        uiState = uiState.copy(
            isEditing    = false,
            editName     = "",
            editTitle    = "",
            editBio      = "",
            editEmail    = "",
            editPhone    = "",
            editLocation = "",
            errorMessage = null
        )
    }

    fun saveProfile() {
        if (uiState.editName.isBlank()) {
            uiState = uiState.copy(errorMessage = "Nama tidak boleh kosong!")
            return
        }
        val updated = uiState.profile.copy(
            name     = uiState.editName.trim(),
            title    = uiState.editTitle.trim(),
            bio      = uiState.editBio.trim(),
            email    = uiState.editEmail.trim(),
            phone    = uiState.editPhone.trim(),
            location = uiState.editLocation.trim()
        )
        uiState = uiState.copy(
            profile      = updated,
            isEditing    = false,
            saveSuccess  = true,
            errorMessage = null,
            editName     = "",
            editTitle    = "",
            editBio      = "",
            editEmail    = "",
            editPhone    = "",
            editLocation = ""
        )
    }

    fun onNameChange    (v: String) { uiState = uiState.copy(editName     = v, errorMessage = null) }
    fun onTitleChange   (v: String) { uiState = uiState.copy(editTitle    = v) }
    fun onBioChange     (v: String) { uiState = uiState.copy(editBio      = v) }
    fun onEmailChange   (v: String) { uiState = uiState.copy(editEmail    = v) }
    fun onPhoneChange   (v: String) { uiState = uiState.copy(editPhone    = v) }
    fun onLocationChange(v: String) { uiState = uiState.copy(editLocation = v) }

    fun toggleDarkMode() {
        uiState = uiState.copy(isDarkMode = !uiState.isDarkMode)
    }

    fun clearSaveSuccess() {
        uiState = uiState.copy(saveSuccess = false)
    }
}

@Composable
fun App() {

    val viewModel = remember { ProfileViewModel() }
    val uiState   = viewModel.uiState

    AnimatedContent(
        targetState = uiState.isDarkMode,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "dark_mode_anim"
    ) { isDark ->
        MaterialTheme(
            colorScheme = if (isDark) buildDarkColors() else buildLightColors()
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color    = MaterialTheme.colorScheme.background
            ) {
                if (uiState.isEditing) {
                    EditProfileScreen(
                        uiState          = uiState,
                        onNameChange     = viewModel::onNameChange,
                        onTitleChange    = viewModel::onTitleChange,
                        onBioChange      = viewModel::onBioChange,
                        onEmailChange    = viewModel::onEmailChange,
                        onPhoneChange    = viewModel::onPhoneChange,
                        onLocationChange = viewModel::onLocationChange,
                        onSave           = viewModel::saveProfile,
                        onCancel         = viewModel::cancelEditing
                    )
                } else {
                    ViewProfileScreen(
                        uiState         = uiState,
                        onEditClick     = viewModel::startEditing,
                        onToggleContact = viewModel::toggleContact,
                        onToggleDark    = viewModel::toggleDarkMode,
                        onClearSuccess  = viewModel::clearSaveSuccess
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewProfileScreen(
    uiState         : ProfileUiState,
    onEditClick     : () -> Unit,
    onToggleContact : () -> Unit,
    onToggleDark    : () -> Unit,
    onClearSuccess  : () -> Unit
) {
    val p = uiState.profile

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            kotlinx.coroutines.delay(3000)
            onClearSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title   = { Text("Profil", fontWeight = FontWeight.SemiBold) },
                actions = {
                    // BONUS: Dark Mode Toggle di TopAppBar — profesional & mudah diakses
                    // STATE HOISTING: isDarkMode dari ViewModel, onToggleDark ke ViewModel
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier              = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text     = if (uiState.isDarkMode) "🌙" else "☀️",
                            fontSize = 14.sp
                        )
                        Switch(
                            checked         = uiState.isDarkMode,
                            onCheckedChange = { onToggleDark() },
                            modifier        = Modifier.scale(0.75f),
                            colors          = SwitchDefaults.colors(
                                checkedThumbColor       = MaterialTheme.colorScheme.primary,
                                checkedTrackColor       = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor     = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor     = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                    TextButton(onClick = onEditClick) {
                        Text(
                            text       = "✏ Edit",
                            color      = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeroHeader(name = p.name, title = p.title, status = p.status)

            Spacer(Modifier.height(20.dp))

            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(value = p.angkatan, label = "Angkatan", modifier = Modifier.weight(1f))
                StatCard(value = p.prodi,    label = "Prodi",    modifier = Modifier.weight(1f))
                StatCard(value = p.projects, label = "Proyek",   modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            BioCard(
                text     = p.bio,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(
                visible = uiState.saveSuccess,
                enter   = expandVertically() + fadeIn(),
                exit    = shrinkVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD6F5E4)),
                    shape  = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text       = "✅  Profil berhasil disimpan!",
                        modifier   = Modifier.padding(14.dp),
                        color      = Color(0xFF0A4A28),
                        fontWeight = FontWeight.Medium,
                        fontSize   = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick  = onToggleContact,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape  = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text       = if (uiState.showContact) "Sembunyikan Kontak"
                    else "Tampilkan Kontak",
                    fontWeight = FontWeight.SemiBold
                )
            }

            AnimatedVisibility(
                visible = uiState.showContact,
                enter   = expandVertically() + fadeIn(),
                exit    = shrinkVertically() + fadeOut()
            ) {
                ContactCard(
                    profile  = p,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text      = "© 2025 · IF25-22017 · ITERA",
                fontSize  = 12.sp,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    uiState          : ProfileUiState,
    onNameChange     : (String) -> Unit,
    onTitleChange    : (String) -> Unit,
    onBioChange      : (String) -> Unit,
    onEmailChange    : (String) -> Unit,
    onPhoneChange    : (String) -> Unit,
    onLocationChange : (String) -> Unit,
    onSave           : () -> Unit,
    onCancel         : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text(
                            "Simpan",
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                "👤  Informasi Dasar",
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.primary
            )
            LabeledTextField(
                label         = "Nama *",
                value         = uiState.editName,
                onValueChange = onNameChange,
                placeholder   = "Nama lengkap",
                isError       = uiState.errorMessage != null && uiState.editName.isBlank(),
                supportingText = if (uiState.errorMessage != null && uiState.editName.isBlank())
                    uiState.errorMessage else null
            )

            LabeledTextField(
                label         = "Titel / Deskripsi Singkat",
                value         = uiState.editTitle,
                onValueChange = onTitleChange,
                placeholder   = "cth: Mobile Developer · ITERA '23"
            )

            LabeledTextField(
                label         = "Bio",
                value         = uiState.editBio,
                onValueChange = onBioChange,
                placeholder   = "Ceritakan tentang dirimu...",
                singleLine    = false,
                minLines      = 3,
                maxLines      = 5
            )

            HorizontalDivider()

            Text(
                "📞  Kontak",
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.primary
            )

            LabeledTextField(
                label         = "Email",
                value         = uiState.editEmail,
                onValueChange = onEmailChange,
                placeholder   = "email@student.itera.ac.id"
            )

            LabeledTextField(
                label         = "Nomor Telepon",
                value         = uiState.editPhone,
                onValueChange = onPhoneChange,
                placeholder   = "+62 8xx-xxxx-xxxx"
            )

            LabeledTextField(
                label         = "Lokasi",
                value         = uiState.editLocation,
                onValueChange = onLocationChange,
                placeholder   = "Kota, Provinsi"
            )

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter   = fadeIn() + expandVertically(),
                exit    = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text     = "⚠️  ${uiState.errorMessage}",
                        modifier = Modifier.padding(12.dp),
                        color    = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp
                    )
                }
            }

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onCancel,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp)
                ) { Text("Batal") }

                Button(
                    onClick  = onSave,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text("Simpan Profil", fontWeight = FontWeight.SemiBold) }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LabeledTextField(
    label          : String,
    value          : String,
    onValueChange  : (String) -> Unit,
    modifier       : Modifier = Modifier,
    placeholder    : String   = "",
    singleLine     : Boolean  = true,
    minLines       : Int      = 1,
    maxLines       : Int      = 1,
    isError        : Boolean  = false,
    supportingText : String?  = null
) {
    OutlinedTextField(
        value          = value,
        onValueChange  = onValueChange,
        label          = { Text(label, fontSize = 13.sp) },
        placeholder    = if (placeholder.isNotEmpty()) {
            { Text(placeholder, fontSize = 13.sp) }
        } else null,
        singleLine     = singleLine,
        minLines       = minLines,
        maxLines       = maxLines,
        isError        = isError,
        supportingText = supportingText?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        modifier       = modifier.fillMaxWidth(),
        shape          = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        ) {
            Text(
                text       = value,
                fontSize   = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = MaterialTheme.colorScheme.primary
            )
            Text(
                text     = label,
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoItem(emoji: String, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) { Text(emoji, fontSize = 18.sp) }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
private fun DarkModeSwitch(
    isDarkMode : Boolean,
    onToggle   : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(if (isDarkMode) "🌙" else " ☀️", fontSize = 16.sp)
            }
            Column {
                Text(
                    if (isDarkMode) "Dark Mode" else "Light Mode",
                    fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    if (isDarkMode) "Tampilan gelap aktif" else "Tampilan terang aktif",
                    fontSize = 11.sp,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(checked = isDarkMode, onCheckedChange = { onToggle() })
    }
}

@Composable
private fun HeroHeader(name: String, title: String, status: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.verticalGradient(colors = listOf(GradientStart, GradientEnd))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter            = painterResource(Res.drawable.profile_pict),
                    contentDescription = "Foto Profil",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(GreenOnline, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text       = name,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = title,
                fontSize  = 13.sp,
                color     = Color.White.copy(alpha = 0.85f),
                fontStyle = FontStyle.Italic
            )

            Spacer(Modifier.height(10.dp))

            // Status chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(GreenOnline, CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text       = status,
                        fontSize   = 12.sp,
                        color      = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun BioCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👤", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Tentang Saya",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            Text(
                text       = text,
                fontSize   = 13.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                textAlign  = TextAlign.Justify
            )
        }
    }
}

@Composable
private fun ContactCard(profile: UserProfile, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📞", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Informasi Kontak",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
            InfoItem("📧", "Email",     profile.email)
            Spacer(Modifier.height(10.dp))
            InfoItem("📱", "Telepon",   profile.phone)
            Spacer(Modifier.height(10.dp))
            InfoItem("📍", "Lokasi",    profile.location)
            Spacer(Modifier.height(10.dp))
            InfoItem("🎓", "Institusi", profile.institution)
        }
    }
}

private fun buildLightColors() = lightColorScheme(
    primary          = PrimaryBlue,
    secondary        = AccentCyan,
    background       = SurfaceLight,
    surface          = CardWhite,
    surfaceVariant   = SurfaceLight,
    onPrimary        = Color.White,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error            = Color(0xFFBA1A1A),
    errorContainer   = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline          = Color(0xFFE0E4F0)
)

private fun buildDarkColors() = darkColorScheme(
    primary          = DarkPrimary,
    secondary        = AccentCyan,
    background       = DarkBackground,
    surface          = DarkSurface,
    surfaceVariant   = DarkSurfaceVar,
    onPrimary        = Color(0xFF003060),
    onBackground     = DarkOnSurface,
    onSurface        = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfVar,
    error            = Color(0xFFFFB4AB),
    errorContainer   = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline          = DarkOutline
)