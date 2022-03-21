
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.view-selector.core.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (core.helpers/component-id :my-extension :view)
  ;  =>
  ;  :my-extension.view-selector/view
  ;
  ; @return (keyword)
  [extension-id component-key]
  ; XXX#5467
  (keyword (str (name extension-id) ".view-selector")
           (name component-key)))

  ;(if-let [namespace (namespace selector-id)]
  ;        (keyword (str namespace "-" (name selector-id) "-" (name component-key)))
  ;        (keyword (str               (name selector-id) "-" (name component-key)))))
