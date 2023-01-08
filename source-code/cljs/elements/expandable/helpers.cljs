
(ns elements.expandable.helpers
    (:require [elements.element.helpers  :as element.helpers]
              [elements.expandable.state :as expandable.state]
              [logic.api                 :refer [nonfalse?]]
              [x.environment.api         :as x.environment]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expanded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ;
  ; @return (boolean)
  [expandable-id]
  (-> @expandable.state/EXPANDED-ELEMENTS expandable-id nonfalse?))

(defn toggle!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  [expandable-id]
  (let [expanded? (-> @expandable.state/EXPANDED-ELEMENTS expandable-id)]
       (if (nil? expanded?)
           (swap! expandable.state/EXPANDED-ELEMENTS assoc  expandable-id false)
           (swap! expandable.state/EXPANDED-ELEMENTS update expandable-id not))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :font-size (keyword)}
  ;
  ; @return (map)
  ; {:data-icon-family (keyword)
  ;  :data-icon-size (keyword)}
  [_ {:keys [icon-color icon-family icon-size]}]
  (-> {:data-icon-family icon-family
       :data-icon-size   icon-size}
      (element.helpers/apply-color :icon-color :data-icon-color icon-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
                 :on-mouse-up #(x.environment/blur-element! expandable-id)}))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
