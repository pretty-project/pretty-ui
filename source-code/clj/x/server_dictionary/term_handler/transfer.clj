
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.transfer
    (:require [server-fruits.io                        :as io]
              [x.server-core.api                       :as a]
              [x.server-dictionary.term-handler.engine :as term-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-transfer!
  :dictionary/transfer-project-dictionary!
  {:data-f      (fn [_] (io/read-edn-file term-handler.engine/PROJECT-DICTIONARY-FILEPATH))
   :target-path [:dictionary :term-handler/data-items]})
