
(ns components.side-menu-footer.views
    (:require [components.copyright-label.views       :as copyright-label.views]
              [components.side-menu-footer.prototypes :as side-menu-footer.prototypes]
              [fruits.random.api                      :as random]
              [fruits.string.api                      :as string]
              [pretty-elements.api                    :as pretty-elements]
              [re-frame.api                           :as r]))

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
   ; @note (tutorials#parametering)
   (fn [_ footer-props]
       (let [] ; footer-props (side-menu-footer.prototypes/footer-props-prototype footer-props)
            [side-menu-footer footer-id footer-props]))))
