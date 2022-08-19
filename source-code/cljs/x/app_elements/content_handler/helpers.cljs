
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-handler.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visible-items->first-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) visible-items
  ;  [{:content-id (keyword)}]
  ;
  ; @example
  ;  (content-handler.helpers/visible-items->first-content-id [{:foo1 :bar1} {:foo2 :bar2 :content-id :baz2}])
  ;  =>
  ;  :baz2
  ;
  ; @return (keyword)
  [visible-items]
  (some :content-id visible-items))
