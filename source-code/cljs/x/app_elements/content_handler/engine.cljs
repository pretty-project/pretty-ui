
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-handler.engine)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visible-items->first-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) visible-items
  ;  [{:content-id (keyword)}]
  ;
  ; @example
  ;  (content-handler.engine/visible-items->first-content-id [{:foo1 :bar1} {:foo2 :bar2 :content-id :baz2}])
  ;  =>
  ;  :baz2
  ;
  ; @return (keyword)
  [visible-items]
  (some :content-id visible-items))
