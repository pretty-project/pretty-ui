
(ns pretty-engine.element.focus.side-effects
    (:require [dom.api                                  :as dom]
              [fruits.hiccup.api                        :as hiccup]
              [pretty-engine.element.state.side-effects :as element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @param (keyword) element-id
  ; @param (keyword) element-props
  [element-id _]
  (let [focus-id (hiccup/value element-id)]
       (if-let [target-element (dom/get-element-by-attribute "data-focus-id" focus-id)]
               (dom/focus-element! target-element))))

(defn blur-element!
  ; @param (keyword) element-id
  ; @param (keyword) element-props
  [element-id _]
  (let [focus-id (hiccup/value element-id)]
       (if-let [target-element (dom/get-element-by-attribute "data-focus-id" focus-id)]
               (dom/blur-element! target-element))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-element-as-focused!
  ; @param (keyword) element-id
  ; @param (keyword) element-props
  [element-id _]
  (element.state.side-effects/update-all-element-state! dissoc :focused?)
  (element.state.side-effects/update-element-state! element-id assoc :focused? true))

(defn unmark-element-as-focused!
  ; @param (keyword) element-id
  ; @param (keyword) element-props
  [element-id _]
  (element.state.side-effects/update-element-state! element-id dissoc :focused?))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-focused
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-focus-f] :as element-props}]
  (mark-element-as-focused! element-id element-props)
  (if on-focus-f (on-focus-f)))

(defn element-left
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-blur-f] :as element-props}]
  (unmark-element-as-focused! element-id element-props)
  (if on-blur-f (on-blur-f)))
