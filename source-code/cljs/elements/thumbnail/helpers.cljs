
(ns elements.thumbnail.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [thumbnail-id {:keys [background-size disabled? on-click style] :as thumbnail-props}]
  (-> (if disabled? {:disabled              true
                     :data-background-size  background-size
                     :data-selectable       false
                     :style                 style}
                    {:data-background-size  background-size
                     :data-click-effect     :opacity
                     :data-selectable       false
                     :style                 style
                     :on-click              #(r/dispatch on-click)
                     :on-mouse-up           #(x.environment/blur-element! thumbnail-id)})
      (pretty-css/border-attributes         thumbnail-props)
      (pretty-css/indent-attributes         thumbnail-props)
      (pretty-css/thumbnail-size-attributes thumbnail-props)))

(defn static-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [thumbnail-id {:keys [background-size height style width] :as thumbnail-props}]
  (-> {:data-background-size  background-size
       :data-selectable       false
       :data-thumbnail-height height
       :data-thumbnail-width  width
       :style                 style}
      (pretty-css/border-attributes thumbnail-props)
      (pretty-css/indent-attributes thumbnail-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  [_ thumbnail-props]
  (-> {} (pretty-css/default-attributes thumbnail-props)
         (pretty-css/outdent-attributes thumbnail-props)))
