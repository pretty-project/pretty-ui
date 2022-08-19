
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.privacy-settings.views
    (:require [settings.cookie-settings.views :rename {body cookie-settings}]
              [x.app-elements.api             :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [cookie-settings])
