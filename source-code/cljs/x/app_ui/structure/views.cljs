
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.structure.views
    (:require [x.app-core.api              :as a :refer [r]]
              [x.app-ui.background.views   :rename {view app-background}]
              [x.app-ui.bubbles            :rename {view app-bubbles}]
              [x.app-ui.header.views       :rename {view app-header}]
              [x.app-ui.locker             :rename {view app-locker}]
              [x.app-ui.popups.views       :rename {view app-popups}]
              [x.app-ui.progress-bar.views :rename {view progress-bar}]
              [x.app-ui.surface            :rename {view app-surface}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- locked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-ui-structure [app-locker]])

(defn- unlocked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-ui-structure [app-background]
                           [app-surface]
                           [app-header]
                           [app-popups]
                           [app-bubbles]
                           [progress-bar]])
                          ;[app-sounds]

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [client-locked? @(a/subscribe [:user/client-locked?])]
          [locked-ui-structure]
          [unlocked-ui-structure]))
