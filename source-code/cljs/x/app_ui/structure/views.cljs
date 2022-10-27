
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.structure.views
    (:require [re-frame.api                   :as r]
              [x.app-ui.bubbles.views         :rename {view app-bubbles}]
              [x.app-ui.locker.views          :rename {view app-locker}]
              [x.app-ui.popups.views          :rename {view app-popups}]
              [x.app-ui.progress-bar.views    :rename {view progress-bar}]
              [x.app-ui.progress-screen.views :rename {view progress-screen}]
              [x.app-ui.sidebar.views         :rename {view app-sidebar}]
              [x.app-ui.surface.views         :rename {view app-surface}]

              ; TEMP
              [reagent.api :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- debug
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (reagent/lifecycles {:component-did-mount (fn [] (println ::debug "mounted"))
                       :reagent-render      (fn [])}))

(defn- locked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-ui-structure [app-locker]])

(defn- unlocked-ui-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-ui-structure [app-surface]
                           [app-sidebar]
                           [app-popups]
                           [app-bubbles]
                          ;[progress-screen]
                           [progress-bar]])
                          ;[app-sounds]
                          ;[debug]

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [client-locked? @(r/subscribe [:user/client-locked?])]
          [locked-ui-structure]
          [unlocked-ui-structure]))
