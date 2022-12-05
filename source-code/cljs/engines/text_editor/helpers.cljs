
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.text-editor.helpers
    (:require [engines.text-editor.config :as config]
              [engines.text-editor.state  :as state]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [editor-id]
  [:engines :text-editor/editor-content editor-id])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-editor-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-INPUT editor-id))

(defn get-editor-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-OUTPUT editor-id))

(defn get-editor-trigger
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-TRIGGER editor-id))

(defn set-editor-output!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) editor-content
  [editor-id editor-content]
  ; HACK#9910
  (swap! state/EDITOR-OUTPUT assoc editor-id editor-content))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizer-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (?) %
  [editor-id %]
  (let [[_ editor-props stored-value] (reagent/arguments %)]
       (when (not= stored-value (get @state/EDITOR-INPUT editor-id))
             (swap! state/EDITOR-INPUT assoc editor-id stored-value))))

(defn synchronizer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {:value-path (vector)}
  [editor-id {:keys [value-path]}]
  (let [stored-value @(r/subscribe [:x.db/get-item value-path])]
       (swap! state/EDITOR-INPUT assoc editor-id stored-value)))

(defn synchronizer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id _]
  (swap! state/EDITOR-INPUT   dissoc editor-id)
  (swap! state/EDITOR-OUTPUT  dissoc editor-id)
  (swap! state/EDITOR-TRIGGER dissoc editor-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-blur-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [_ _]
  (r/dispatch-sync [:x.environment/quit-type-mode!]))

(defn on-focus-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [_ _]
  (r/dispatch-sync [:x.environment/set-type-mode!]))

(defn on-change-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {:value-path (vector)}
  ; @param (string) editor-content
  [editor-id {:keys [value-path]} editor-content]
  ; Az on-change-f függvény a text-editor aktuális tartalmát ...
  ; ... a set-editor-content! függvénnyel az EDITOR-CONTENTS atomba írja.
  ; ... a dispatch-last függvénnyel a value-path Re-Frame adatbázis útvonalra írja,
  ;    ha a felhasználó már befejezte a gépelést.
  (set-editor-output! editor-id editor-content)
  (r/dispatch-last    config/TYPE-ENDED-AFTER [:x.db/set-item! value-path editor-content]))
