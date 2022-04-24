
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.helpers
    (:require [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ;  {:default-title (metamorphic-content)(opt)
  ;   :title (metamorphic-content)}
  [_ {:keys [default-title title]}]
  (letfn [(f [intersecting?] (if intersecting? (a/fx [:ui/set-header-title! default-title])
                                               (a/fx [:ui/set-header-title! title])))]
         (environment/setup-intersection-observer! "x-app-header--title-sensor" f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sensor-id
  [_]
  (environment/remove-intersection-observer! "x-app-header--title-sensor"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  [sensor-id sensor-props]
  ; Ha a title-sensor komponens {:title ...} paramétere megváltozik, akkor szükséges az intersection-observer
  ; figyelőt újra létrehozni a megváltozott {:title ...} paraméter átadásával.
  ; Pl.: Ha a title-sensor komponens egy Re-Frame feliratkozás kimenetét kapja meg {:title ...} paraméterként,
  ;      ami a komponens React-fába csatolása után megváltozik.
  (sensor-will-unmount-f sensor-id)
  (sensor-did-mount-f    sensor-id sensor-props))
