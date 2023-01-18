
(ns elements.anchor.attributes
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [anchor-id {:keys [disabled? href on-click style] :as anchor-props}]
  (-> (if disabled? {:class               :e-anchor--body
                     :data-letter-spacing :auto
                     :data-text-overflow  :no-wrap
                     :disabled            true
                     :style               style}
                    {:class               :e-anchor--body
                     :data-click-effect   :opacity
                     :data-letter-spacing :auto
                     :data-text-overflow  :no-wrap
                     :href                href
                     :style               style
                     :on-click            #(r/dispatch on-click)
                     :on-mouse-up         #(x.environment/blur-element!)})
      (pretty-css/color-attributes  anchor-props)
      (pretty-css/font-attributes   anchor-props)
      (pretty-css/indent-attributes anchor-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ anchor-props]
  (-> {:class :e-anchor}
      (pretty-css/default-attributes anchor-props)
      (pretty-css/outdent-attributes anchor-props)))
