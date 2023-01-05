
(ns elements.thumbnail.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
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
  (merge (element.helpers/element-indent-attributes thumbnail-id thumbnail-props)
         (thumbnail-layout-attributes               thumbnail-id thumbnail-props)
         (if disabled? {:data-selectable false
                        :disabled        true
                        :style           style}
                       {:data-clickable  true
                        :data-selectable false
                        :on-click        #(r/dispatch on-click)
                        :on-mouse-up     #(element.side-effects/blur-element! thumbnail-id)
                        :style           style})))

(defn static-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [thumbnail-id {:keys [style] :as thumbnail-props}]
  (merge (element.helpers/element-indent-attributes thumbnail-id thumbnail-props)
         (thumbnail-layout-attributes               thumbnail-id thumbnail-props)
         {:data-selectable false
          :style           style}))

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  [thumbnail-id thumbnail-props]
  (merge (element.helpers/element-default-attributes thumbnail-id thumbnail-props)
         (element.helpers/element-outdent-attributes thumbnail-id thumbnail-props)))
