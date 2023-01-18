
(ns pretty-css.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; The normalizer styles are the most axiomatic principals of the UI, therefore
; it has to be at the very first place in the CSS cascade! {:weight -1}
(x.core/reg-lifecycles! ::normalize
  {:on-server-boot {:dispatch-n [[:x.environment/add-css! {:uri "/normalize.min.css" :weight -1
                                                           :dev-resources ["public/normalize"]}]
                                 [:x.environment/add-css! {:uri "/pretty-css.min.css" :weight 1
                                                           :dev-resources ["public/pretty-css"]}]
                                 [:x.environment/add-css! {:uri "/material-icons.min.css" :weight 1
                                                           :dev-resources ["public/material-icons"]}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/icon?family=Material+Icons"}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined"}]
                                 [:x.environment/add-css! {:uri "/material-symbols.min.css" :weight 1
                                                           :dev-resources ["public/material-symbols"]}]
                                 [:x.environment/add-css! {:uri "https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"}]]}})
