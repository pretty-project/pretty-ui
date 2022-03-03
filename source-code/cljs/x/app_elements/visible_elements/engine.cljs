
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.visible-elements.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- visible-items->first-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) visible-items
  ;  [{:content-id (keyword)}]
  ;
  ; @example
  ;  (visible-elements.engine/visible-items->first-content-id [{:foo1 :bar1} {:foo2 :bar2 :content-id :baz2}])
  ;  =>
  ;  :baz2
  ;
  ; @return (keyword)
  [visible-items]
  (vector/get-first-match-item visible-items #(when-let [content-id (:content-id %1)]
                                                        (return content-id))))

(defn on-hide-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (function)
  [surface-id]
  #(a/dispatch [:elements/empty-surface! surface-id]))

(defn on-show-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (function)
  [surface-id content-id]
  #(a/dispatch [:elements/set-content! surface-id content-id]))
