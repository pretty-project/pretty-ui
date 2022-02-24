
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.privacy-settings.views
    (:require [x.app-elements.api :as elements]
              [app-extensions.settings.cookie-settings.views :rename {body cookie-settings}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [cookie-settings])
