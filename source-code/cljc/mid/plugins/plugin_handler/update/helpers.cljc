
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.update.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ; @param (string) item-id
  ;
  ; @example
  ;  (update.helpers/dialog-id :my-plugin :delete "my-item")
  ;  =>
  ;  :my-plugin-delete-my-item-dialog
  ;
  ; @return (map)
  [plugin-id action-key item-id]
  ; XXX#0401
  ; Ha a plugin több különböző művelethez tartozó értesítést jelenít meg egyszerre,
  ; akkor szükséges az értesítéseket egyedi azonosítóval ellátni!
  (if-let [namespace (namespace plugin-id)]
          (keyword namespace (str (name plugin-id) "-" (name action-key) "-" item-id "-dialog"))
          (keyword           (str (name plugin-id) "-" (name action-key) "-" item-id "-dialog"))))

(defn items-dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ; @param (strings in vector) item-ids
  ;
  ; @example
  ;  (update.helpers/dialog-id :my-plugin :delete ["my-item" "your-item"])
  ;  =>
  ;  :my-plugin-delete-["my-item" "your-item"]-dialog
  ;
  ; @return (map)
  [plugin-id action-key item-ids]
  ; XXX#0401
  (if-let [namespace (namespace plugin-id)]
          (keyword namespace (str (name plugin-id) "-" (name action-key) "-" item-ids "-dialog"))
          (keyword           (str (name plugin-id) "-" (name action-key) "-" item-ids "-dialog"))))
