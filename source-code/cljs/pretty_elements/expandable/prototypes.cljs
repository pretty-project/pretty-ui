
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
  (let [on-click-f (fn [] (expandable.side-effects/toggle-visibility! expandable-id expandable-props))]
       (if (expandable.env/surface-visible? expandable-id expandable-props)
           (-> {:on-click-f on-click-f :icon :expand_less :icon-position :right} (merge button))
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
  (-> {:horizontal-align :left} (merge surface)))

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
  (-> expandable-props))
