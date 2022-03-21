
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.transfer.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @example
  ;  (transfer.helpers/transfer-id :my-namespace/my-plugin)
  ;  =>
  ;  :my-namespace.my-plugin/transfer-selector-props
  ;
  ; @return (keyword)
  [plugin-id]
  (if-let [namespace (namespace plugin-id)]
          (keyword (str namespace "." (name plugin-id)) "transfer-plugin-props")
          (keyword (str               (name plugin-id)) "transfer-plugin-props")))
