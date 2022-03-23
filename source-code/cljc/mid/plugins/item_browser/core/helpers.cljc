
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-browser.core.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (core.helpers/component-id :my-extension :my-type :view)
  ;  =>
  ;  :my-extension.my-type-browser/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           (name component-key)))

  ;(if-let [namespace (namespace browser-id)]
  ;        (keyword (str namespace "-" (name browser-id) "-" (name component-key)))
  ;        (keyword (str               (name browser-id) "-" (name component-key)))))
