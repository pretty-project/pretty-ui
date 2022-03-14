
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sound-id->catalog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sound-id
  ;
  ; @example
  ;  (sounds.engine/sound-id->catalog-id :my-sound)
  ;  =>
  ;  "x-app-sound--my-sound"
  ;
  ; @return (string)
  [sound-id]
  (str "x-app-sound--" (name sound-id)))
