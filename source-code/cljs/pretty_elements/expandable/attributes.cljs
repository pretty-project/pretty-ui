
(ns pretty-elements.expandable.attributes
    (:require [dom.api                                 :as dom]
              [pretty-build-kit.api                    :as pretty-build-kit]
              [pretty-elements.expandable.side-effects :as expandable.side-effects]))

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
      (pretty-build-kit/icon-attributes expandable-props)))

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
      (pretty-build-kit/indent-attributes expandable-props)
      (pretty-build-kit/style-attributes  expandable-props)))

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
      (pretty-build-kit/class-attributes   expandable-props)
      (pretty-build-kit/outdent-attributes expandable-props)
      (pretty-build-kit/state-attributes   expandable-props)))
