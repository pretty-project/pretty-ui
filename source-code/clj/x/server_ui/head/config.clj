
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
                       {:uri "/css/x/app-fonts.css"}
                       {:uri "/css/normalize.css"}
                       {:uri "/css/x/animations.css"}
                       ; ...
                       {:uri "/css/x/app-ui-profiles.css"}
                       {:uri "/css/x/app-ui-themes.css"}
                       ; ...
                       {:uri "/css/x/app-ui-structure.css"}
                       {:uri "/css/x/app-ui-animations.css"}
                       {:uri "/css/x/app-ui-graphics.css"}
                       ; ...
                       {:uri "/css/x/app-layouts.css"}
                       {:uri "/css/x/app-elements.css"}
                       {:uri "/css/x/app-views.css"}
                       ; Using self hosted Font Awesome icons
                       {:uri "/icons/fontawesome-free-5.15.1-web/css/all.min.css"}
                       ; XXX#8857
                       ; Using Material Icons via Google Web Fonts
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}
                       {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}])
