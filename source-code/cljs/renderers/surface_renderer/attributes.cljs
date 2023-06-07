
(ns renderers.surface-renderer.attributes
    (:require [hiccup.api :as hiccup]
              [noop.api   :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-content-attributes
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :id (string)
  ;  :key (string)}
  [_ surface-id]
  {:class :r-surface-renderer--content
   :id  (hiccup/value surface-id)
   :key (hiccup/value surface-id)})
