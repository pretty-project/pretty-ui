
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
;  [{:core-js (string)(opt)
;    :uri (string)}]
(def SYSTEM-CSS-PATHS [; ...
                       {:uri "/css/normalize.css"}
                       {:uri "/css/animations.css"}
                       {:uri "/css/defaults.css"}

                       ; ...
                       {:uri "/css/x/app-ui-profiles.css"}
                       {:uri "/css/x/app-ui-themes.css"}
                       {:uri "/css/x/app-ui-structure.css"}
                       {:uri "/css/x/app-ui-animations.css"}
                       {:uri "/css/x/app-elements.css"}

                       ; Montserrat font
                       {:uri "/css/montserrat-font.css"}

                       ; Font Awesome icons
                       {:uri "/css/fontawesome-icons.css"}
                       {:uri "/icons/fontawesome-free-5.15.1-web/css/all.min.css"}

                       ; Material Icons
                       ; A Material Icons ikonkészlet .ttf formátumban hosztolva nem minden mobil
                       ; böngészőben jelent meg, .woff formátumban pedig nem tartalmazta a teljes,
                       ; legfrissebb készletet (2020.10.11)
                       ; Emiatt van a Google Fonts szerverről hosztolva.
                       {:uri "/css/material-design-icons.css"}
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}])
