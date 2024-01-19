
(ns pretty-elements.core.side-effects
    (:require [dom.api                    :as dom]
              [fruits.hiccup.api          :as hiccup]
              [fruits.map.api             :as map]
              [pretty-elements.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @ignore
  ;
  ; @param (keyword) element-id
  [element-id]
  (let [focus-id (hiccup/value element-id)]
       (if-let [target-element (dom/get-element-by-attribute "data-focus-id" focus-id)]
               (dom/focus-element! target-element))))

(defn blur-element!
  ; @ignore
  ;
  ; @param (keyword) element-id
  [element-id]
  (let [focus-id (hiccup/value element-id)]
       (if-let [target-element (dom/get-element-by-attribute "data-focus-id" focus-id)]
               (dom/blur-element! target-element))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-all-element-state!
  ; @ignore
  ;
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ; (update-all-element-state! dissoc :my-key)
  [f & params]
  (letfn [(f0 [%] (map/->values % f1))
          (f1 [%] (apply f % params))]
         (swap! core.state/ELEMENT-STATE f0)))

(defn update-element-state!
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ; (update-element-state! :my-element assoc :my-key "My value")
  [element-id f & params]
  (letfn [(f0 [%] (apply f % params))]
         (swap! core.state/ELEMENT-STATE update element-id f0)))

(defn clear-element-state!
  ; @ignore
  ;
  ; @param (keyword) element-id
  ;
  ; @usage
  ; (clear-element-state! :my-element)
  [element-id]
  (swap! core.state/ELEMENT-STATE dissoc element-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-did-mount
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [_ {:keys [on-mount-f]}]
  (if on-mount-f (on-mount-f)))

(defn element-will-unmount
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-unmount-f]}]
  (clear-element-state! element-id)
  (if on-unmount-f (on-unmount-f)))
