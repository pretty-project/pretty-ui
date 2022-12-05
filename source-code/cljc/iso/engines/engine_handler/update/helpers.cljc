
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.engine-handler.update.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ; @param (string) item-id
  ;
  ; @example
  ; (dialog-id :my-engine :delete "my-item")
  ; =>
  ; :my-engine-delete-my-item-dialog
  ;
  ; @return (map)
  [engine-id action-key item-id]
  ; XXX#0401
  ; Ha az engine több különböző művelethez tartozó értesítést jelenít meg egyszerre,
  ; akkor szükséges az értesítéseket egyedi azonosítóval ellátni!
  (if-let [namespace (namespace engine-id)]
          (keyword namespace (str (name engine-id) "-" (name action-key) "-" item-id "-dialog"))
          (keyword           (str (name engine-id) "-" (name action-key) "-" item-id "-dialog"))))

(defn items-dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ; @param (strings in vector) item-ids
  ;
  ; @example
  ; (dialog-id :my-engine :delete ["my-item" "your-item"])
  ; =>
  ; :my-engine-delete-["my-item" "your-item"]-dialog
  ;
  ; @return (map)
  [engine-id action-key item-ids]
  ; XXX#0401
  (if-let [namespace (namespace engine-id)]
          (keyword namespace (str (name engine-id) "-" (name action-key) "-" item-ids "-dialog"))
          (keyword           (str (name engine-id) "-" (name action-key) "-" item-ids "-dialog"))))
