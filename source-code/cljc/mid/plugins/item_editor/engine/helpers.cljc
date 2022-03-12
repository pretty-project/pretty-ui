
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-editor.engine.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (collection-name :my-extension :my-type)
  ;  =>
  ;  "my-extension"
  ;
  ; @return (string)
  [extension-id _]
  (name extension-id))

(defn value-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (item-editor/value-path :my-extension :my-type :my-item)
  ;  =>
  ;  [:my-extension :item-editor/data-items :my-item]
  ;
  ; @return (item-path vector)
  [extension-id _ item-key]
  [extension-id :item-editor/data-items item-key])

(defn add-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (add-item-label :my-extension :my-type)
  ;  =>
  ;  :add-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (keyword (str "add-" (name item-namespace))))

(defn edit-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (edit-item-label :my-extension :my-type)
  ;  =>
  ;  :edit-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (keyword (str "edit-" (name item-namespace))))

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (new-item-label :my-extension :my-type)
  ;  =>
  ;  :new-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "new-" (name item-namespace))))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (new-item-label :my-extension :my-type)
  ;  =>
  ;  :unnamed-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "unnamed-" (name item-namespace))))
