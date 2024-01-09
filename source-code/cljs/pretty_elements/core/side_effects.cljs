
(ns pretty-elements.core.side-effects
    (:require [dom.api :as dom]
              [fruits.hiccup.api :as hiccup]))

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
