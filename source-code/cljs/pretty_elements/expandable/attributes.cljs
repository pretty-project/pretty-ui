
(ns pretty-elements.expandable.attributes
    (:require [dom.api                                 :as dom]
              
              [pretty-elements.expandable.side-effects :as expandable.side-effects]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.content/icon-attributes expandable-props)))

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
  (let [on-click-f #(expandable.side-effects/toggle! expandable-id)]
       (if disabled? {:class                :pe-expandable--header
                      :disabled             true}
                     {:class                :pe-expandable--header
                      :data-click-effect    :opacity
                      :data-text-selectable false
                      :on-click             on-click-f
                      :on-mouse-up          dom/blur-active-element!})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-body-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ expandable-props]
  (-> {:class :pe-expandable--body}
      (pretty-css.layout/indent-attributes expandable-props)
      (pretty-css.basic/style-attributes  expandable-props)))

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
      (pretty-css.basic/class-attributes   expandable-props)
      (pretty-css.layout/outdent-attributes expandable-props)
      (pretty-css.basic/state-attributes   expandable-props)
      (pretty-css.appearance/theme-attributes   expandable-props)))
