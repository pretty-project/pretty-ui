
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a.helpers
    (:require [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  [_]
  (letfn [(f [intersecting?] (environment/set-element-attribute! "x-layout-a--content-header" "data-sticky" (not intersecting?))
                             (println (str "x-layout-a--content-header--sensor " intersecting?)))]
         (environment/setup-intersection-observer! "x-layout-a--content-header--sensor" f)))

(defn content-footer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  [_]
  (letfn [(f [intersecting?] (environment/set-element-attribute! "x-layout-a--content-footer" "data-sticky" (not intersecting?)))]
         (environment/setup-intersection-observer! "x-layout-a--content-footer--sensor" f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-header-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  [_]
  (environment/remove-intersection-observer! "x-layout-a--content-header--sensor"))

(defn content-footer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  [_]
  (environment/remove-intersection-observer! "x-layout-a--content-footer--sensor"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :data-disabled (boolean)
  ;   :data-horizontal-align (keyword)
  ;   :data-min-width (keyword)
  ;   :style (map)}
  [_ {:keys [class disabled? horizontal-align min-width style]}]
  (cond-> {} class             (assoc :class                 class)
             style             (assoc :style                 style)
             min-width         (assoc :data-min-width        min-width)
             horizontal-align  (assoc :data-horizontal-align horizontal-align)
             (some? disabled?) (assoc :data-disabled         disabled?)))
