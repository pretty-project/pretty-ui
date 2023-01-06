
(ns layouts.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/merge-css! :layouts/style.css
                                              {:resources ["public/css/layouts/popup-a.css"
                                                           "public/css/layouts/popup-b.css"
                                                           "public/css/layouts/sidebar-a.css"
                                                           "public/css/layouts/sidebar-b.css"
                                                           "public/css/layouts/surface-a.css"]}]})
