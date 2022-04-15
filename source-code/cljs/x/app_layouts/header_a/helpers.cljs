
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.header-a.helpers
    (:require [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  [_ {:keys [label]}]
  (letfn [(f [intersecting?] (if intersecting? (a/fx [:ui/remove-header-title!])
                                               (a/fx [:ui/set-header-title! label])))]
         (environment/setup-intersection-observer! "x-header-a--sensor" f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  [_]
  (environment/remove-intersection-observer! "x-header-a--sensor"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  [header-id header-props]
  ; Ha a header komponens label paramétere megváltozik, akkor szükséges az intersection-observer
  ; figyelőt újra létrehozni a megváltozott label paraméter átadásával.
  ; Pl.: Ha a header komponens egy Re-Frame feliratkozás kimenetét kapja meg label paraméterként, ami
  ;      a komponens React-fába csatolása után megváltozik.
  (header-will-unmount-f header-id)
  (header-did-mount-f    header-id header-props))
