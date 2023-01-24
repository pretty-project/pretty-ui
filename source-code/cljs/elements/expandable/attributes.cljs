
(ns elements.expandable.attributes
    (:require [dom.api                     :as dom]
              [elements.expandable.helpers :as expandable.helpers]
              [pretty-css.api              :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------


(defn expandable-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ expandable-props]
  (-> {:class :e-expandable--icon}
      (pretty-css/icon-attributes expandable-props)))

(defn expandable-header-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [expandable-id {:keys [disabled?]}]
  (if disabled? {:class             :e-expandable--header
                 :disabled          true}
                {:class             :e-expandable--header
                 :data-click-effect :opacity
                 :data-selectable   false
                 :on-click    #(expandable.helpers/toggle! expandable-id)
                 :on-mouse-up #(dom/blur-active-element!)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-body-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as expandable-props}]
  (-> {:class :e-expandable--body
       :style style}
      (pretty-css/indent-attributes expandable-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {}
  [_ expandable-props]
  (-> {:class :e-expandable}
      (pretty-css/default-attributes expandable-props)
      (pretty-css/outdent-attributes expandable-props)))