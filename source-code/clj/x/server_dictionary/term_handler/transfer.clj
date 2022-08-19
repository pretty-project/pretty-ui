

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.transfer
    (:require [server-fruits.io                        :as io]
              [x.server-core.api                       :as a]
              [x.server-dictionary.term-handler.config :as term-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer!
  :dictionary/transfer-project-dictionary!
  {:data-f      (fn [_] (io/read-edn-file term-handler.config/PROJECT-DICTIONARY-FILEPATH))
   :target-path [:dictionary :term-handler/data-items]})
