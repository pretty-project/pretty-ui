
(ns elements.thumbnail.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :border-radius (keyword)
  ;  :height (keyword)
  ;  :width (keyword)}
  ;
  ; @return (map)
  ; {:data-background-size (keyword)
  ;  :data-border-radius (keyword)
  ;  :data-thumbnail-height (keyword)
  ;  :data-thumbnail-width (keyword)}
  [_ {:keys [background-size border-radius height width]}]
  {:data-background-size  background-size
   :data-border-radius    border-radius
   :data-thumbnail-height height
   :data-thumbnail-width  width})

(defn toggle-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [thumbnail-id {:keys [disabled? on-click style] :as thumbnail-props}]
  (merge (pretty-css/indent-attributes             thumbnail-props)
         (thumbnail-layout-attributes thumbnail-id thumbnail-props)
         {:data-selectable false
          :style           style}
         (if disabled? {:disabled          true}
                       {:data-click-effect :opacity
                        :on-click          #(r/dispatch on-click)
                        :on-mouse-up       #(x.environment/blur-element! thumbnail-id)})))

(defn static-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-selectable (boolean)
  ;  :style (map)}
  [thumbnail-id {:keys [style] :as thumbnail-props}]
  (merge (pretty-css/indent-attributes             thumbnail-props)
         (thumbnail-layout-attributes thumbnail-id thumbnail-props)
         {:data-selectable false
          :style           style}))

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  [_ thumbnail-props]
  (merge (pretty-css/default-attributes thumbnail-props)
         (pretty-css/outdent-attributes thumbnail-props)))
