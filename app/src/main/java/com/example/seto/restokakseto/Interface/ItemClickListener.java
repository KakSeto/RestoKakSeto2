package com.example.seto.restokakseto.Interface;

import android.view.View;

/** ---------- CLASS INTERFACE ------------- **/

 // hanya mengandung deklarasi method tanpa memiliki implementasi
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
