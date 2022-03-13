
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-browser.transfer.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (transfer.helpers/transfer-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-browser/transfer-browser-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "transfer-browser-props"))
