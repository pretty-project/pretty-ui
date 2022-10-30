
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.content.sample
    (:require [x.app-components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a {:content ...} tulajdonságon kívül más beállítás nem kerül átadásra a content
; komponens számára, akkor a {:content ...} tulajdonság rövidített formában is átadható
(defn my-content
  []
  [:<> [x.components/content {:content :username}]
       [x.components/content           :username]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn yor-content
  []
  [:<> [x.components/content {:content "% items uploading ..."     :replacements ["5"]}]
       [x.components/content {:content "%1 downloaded of %2 items" :replacements ["5" "10"]}]
       [x.components/content {:content :n-items-uploading          :replacements ["5"]}]
       [x.components/content {:content :npn-items-downloaded       :replacements ["5" "10"]}]])
