
(ns icons.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [; Font Awesome icons
                                 [:x.environment/add-css! {:uri "/icons/fontawesome-free-5.15.1-web/css/all.min.css"}]
                                 ; Material Icons
                                 ; A Material Icons ikonkészlet .ttf formátumban hosztolva nem minden mobil
                                 ; böngészőben jelent meg, .woff formátumban pedig nem tartalmazta a teljes,
                                 ; legfrissebb készletet (2020.10.11)
                                 ; Emiatt van a Google Fonts szerverről hosztolva.
                                 [:x.environment/add-css! {:uri "/icons/material-design-icons.css"}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"}]]}})
