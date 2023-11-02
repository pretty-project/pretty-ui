
(ns components.side-menu-footer.views
    (:require [components.copyright-label.views       :as copyright-label.views]
              [components.side-menu-footer.prototypes :as side-menu-footer.prototypes]
              [pretty-elements.api                           :as pretty-elements]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [string.api                             :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- side-menu-footer
  ; @ignore
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  [footer-id footer-props]
  [:div.c-side-menu-footer {:data-indent-bottom :xxs}
                           [copyright-label.views/component {}]])

(defn component
  ; @param (keyword)(opt) footer-id
  ; @param (map) footer-props
  ; {}
  ;
  ; @usage
  ; [side-menu-footer {...}]
  ;
  ; @usage
  ; [side-menu-footer :my-side-menu-footer {...}]
  ([footer-props]
   [component (random/generate-keyword) footer-props])

  ([footer-id footer-props]
   (fn [_ footer-props] ; XXX#0106 (README.md#parametering)
       (let [] ; footer-props (side-menu-footer.prototypes/footer-props-prototype footer-props)
            [side-menu-footer footer-id footer-props]))))
