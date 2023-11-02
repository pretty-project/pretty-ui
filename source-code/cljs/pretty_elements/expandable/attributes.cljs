
(ns pretty-elements.expandable.attributes
    (:require [dom.api                          :as dom]
              [pretty-elements.expandable.side-effects :as expandable.side-effects]
              [pretty-css.api                   :as pretty-css]))

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
  (-> {:class :pe-expandable--icon}
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
  (if disabled? {:class             :pe-expandable--header
                 :disabled          true}
                {:class             :pe-expandable--header
                 :data-click-effect :opacity
                 :data-selectable   false
                 :on-click    #(expandable.side-effects/toggle! expandable-id)
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
  (-> {:class :pe-expandable--body
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
  (-> {:class :pe-expandable}
      (pretty-css/default-attributes expandable-props)
      (pretty-css/outdent-attributes expandable-props)))
