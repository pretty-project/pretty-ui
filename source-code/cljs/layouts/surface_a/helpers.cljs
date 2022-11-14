
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.surface-a.helpers
    (:require [layouts.surface-a.state :as state]
              [reagent.api             :as reagent]
              [x.environment.api       :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-sensor-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sensor-props
  ;  {:title (metamorphic-content)}
  [{:keys [title]}]
  (letfn [(f [intersecting?] (if intersecting? (reset! state/HEADER-TITLE-VISIBLE? false)
                                               (reset! state/HEADER-TITLE-VISIBLE? true)))]
         (reset! state/HEADER-TITLE title)
         (x.environment/setup-intersection-observer! "surface-a--title-sensor" f)))

(defn title-sensor-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (x.environment/remove-intersection-observer! "surface-a--title-sensor"))

(defn title-sensor-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) %
  [%]
  ; Ha a title-sensor komponens {:title ...} paramétere megváltozik, akkor szükséges az intersection-observer
  ; figyelőt újra létrehozni a megváltozott {:title ...} paraméter átadásával.
  ; Pl. Ha a title-sensor komponens egy Re-Frame feliratkozás kimenetét kapja meg {:title ...} paraméterként,
  ;      ami a komponens React-fába csatolása után megváltozik.
  (let [[sensor-props] (reagent/arguments %)]
       (title-sensor-will-unmount-f)
       (title-sensor-did-mount-f sensor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (letfn [(f [intersecting?] (if intersecting? (reset! state/HEADER-SHADOW-VISIBLE? false)
                                               (reset! state/HEADER-SHADOW-VISIBLE? true)))]
         (x.environment/setup-intersection-observer! "surface-a--header-sensor" f)))

(defn header-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (x.environment/remove-intersection-observer! "surface-a--header-sensor"))
