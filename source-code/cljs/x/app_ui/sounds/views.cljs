
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds.views)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-sounds [:audio#x-app-sound--click-1 [:source {:src "/sounds/click-1.ogg" :type "audio/ogg"}]
                                                  [:source {:src "/sounds/click-1.mp3" :type "audio/mp3"}]]])
