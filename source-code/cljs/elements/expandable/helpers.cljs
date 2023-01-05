
(ns elements.expandable.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [elements.expandable.state     :as expandable.state]
              [logic.api                     :refer [nonfalse?]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expanded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ;
  ; @return (boolean)
  [expandable-id]
  (-> @expandable.state/EXPANDS expandable-id nonfalse?))

(defn toggle!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  [expandable-id]
  (let [expanded? (-> @expandable.state/EXPANDS expandable-id)]
       (if (nil? expanded?)
           (swap! expandable.state/EXPANDS assoc  expandable-id false)
           (swap! expandable.state/EXPANDS update expandable-id not))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  [expandable-id {:keys [style] :as expandable-props}]
  (merge (element.helpers/element-indent-attributes expandable-id expandable-props)
         {:style style}))

(defn expandable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  [expandable-id expandable-props]
  (merge (element.helpers/element-default-attributes expandable-id expandable-props)
         (element.helpers/element-outdent-attributes expandable-id expandable-props)))

(defn expandable-header-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  [expandable-id {:keys [disabled?]}]
  (if disabled? {:disabled        true}
                {:data-clickable  true
                 :data-selectable false
                 :on-click    #(toggle! expandable-id)
                 :on-mouse-up #(element.side-effects/blur-element! expandable-id)}))
