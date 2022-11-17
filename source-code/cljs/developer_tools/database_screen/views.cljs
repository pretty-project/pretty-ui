
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.database-screen.views
    (:require [pretty.print :as pretty]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @usage
  ;  [database-screen]
  []
  (let [db @(r/subscribe [:x.db/get-db])]
       [:div#x-developer-tools--database-screen [:pre (pretty/mixed->string db)]]))
