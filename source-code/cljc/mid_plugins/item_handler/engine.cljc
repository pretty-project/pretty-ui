
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-handler.engine)



;; -- Public helpers ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-handler/synchronize-handler!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "synchronize-handler!"))



;; -- Private helpers ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :add-items)
  ;  =>
  ;  "my-extension.my-type-handler/add-items!"
  ;
  ; @return (string)
  [extension-id item-namespace action-key]
  (str (name extension-id)   "."
       (name item-namespace) "-handler/"
       (name action-key)     "!"))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type :get-item)
  ;  =>
  ;  :my-extension.my-type-handler/get-item
  ;
  ; @return (keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-handler")
           (str (name action-key))))

(defn dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :delete-items)
  ;  =>
  ;  :my-extension.my-type-handler/delete-items-dialog
  ;
  ; @return (namespaced keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-handler")
           (str (name action-key)     "-dialog")))
