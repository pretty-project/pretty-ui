
(ns pretty-elements.expandable.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]
              [pretty-elements.expandable.side-effects :as expandable.side-effects]
              [pretty-elements.expandable.env :as expandable.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:button (map)(opt)}
  ;
  ; @return (map)
  [expandable-id {:keys [button] :as expandable-props}]
  (if (expandable.env/surface-mounted? expandable-id expandable-props)
      (let [on-click-f (fn [] (expandable.side-effects/collapse-content! expandable-id))]
           (-> {:on-click-f on-click-f :icon :expand_less :icon-position :right} (merge button)))
      (let [on-click-f (fn [] (expandable.side-effects/expand-content! expandable-id))]
           (-> {:on-click-f on-click-f :icon :expand_more :icon-position :right} (merge button)))))

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:surface (map)(opt)}
  ;
  ; @return (map)
  [_ {:keys [surface]}]
  (-> surface))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  [_ expandable-props]
  (-> expandable-props (pretty-elements.properties/default-size-props {:height :content :width :content})))
