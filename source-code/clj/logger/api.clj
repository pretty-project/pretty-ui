
(ns logger.api
    (:require [logger.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; logger.engine
(def init!  engine/init!)
(def write! engine/write!)
